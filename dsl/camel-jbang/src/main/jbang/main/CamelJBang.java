///usr/bin/env jbang "$0" "$@" ; exit $?

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//REPOS mavencentral,apache=https://repository.apache.org/snapshots
//DEPS org.apache.camel:camel-bom:${camel.jbang.version}@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-core-model
//DEPS org.apache.camel:camel-api
//DEPS org.apache.camel:camel-main
//DEPS org.apache.camel:camel-kamelet-main
//DEPS org.apache.camel:camel-file-watch
//DEPS org.apache.camel:camel-resourceresolver-github
//DEPS org.apache.logging.log4j:log4j-api:${camel.jbang.log4j2.version}
//DEPS org.apache.logging.log4j:log4j-core:${camel.jbang.log4j2.version}
//DEPS org.apache.logging.log4j:log4j-slf4j-impl:${camel.jbang.log4j2.version}
//DEPS info.picocli:picocli:${camel.jbang.picocli.version}

package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.github.GitHubResourceResolver;
import org.apache.camel.main.KameletMain;
import org.apache.camel.spi.Resource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "run", description = "Run a Kamelet")
class Run implements Callable<Integer> {
    private CamelContext context;

    @Parameters(description = "The path to the kamelet binding", arity = "1")
    private String binding;

    @Option(names = {"--debug-level"}, defaultValue = "info", description = "Default debug level")
    private String debugLevel;

    class ShutdownRoute extends RouteBuilder {
        private File lockFile;

        public ShutdownRoute(File lockFile) {
            this.lockFile = lockFile;
        }

        public void configure() {
            fromF("file-watch://%s?events=DELETE&antInclude=%s", lockFile.getParent(), lockFile.getName())
                .process(p -> context.shutdown());
        }
    }

    @Override
    public Integer call() throws Exception {
        switch (debugLevel) {
            case "trace": Configurator.setRootLevel(Level.TRACE); break;
            case "debug": Configurator.setRootLevel(Level.DEBUG); break;
            case "info": Configurator.setRootLevel(Level.INFO); break;
            case "warn": Configurator.setRootLevel(Level.WARN); break;
            case "fatal": Configurator.setRootLevel(Level.FATAL); break;
            default: {
                System.err.println("Invalid debug level " + debugLevel);
                return 1;
            }
        }

        File bindingFile = new File(binding);
        if (!bindingFile.exists()) {
            System.err.println("The binding file does not exist");

            return 1;
        }

        System.setProperty("camel.main.routes-include-pattern", "file:" + binding);
        System.setProperty("camel.main.name", "CamelJBang");

        System.out.println("Starting Camel JBang!");
        KameletMain main = new KameletMain();

        main.configure().addRoutesBuilder(new ShutdownRoute(createLockFile()));
        main.start();
        context = main.getCamelContext();

        main.run();
        return 0;
    }

    public File createLockFile() throws IOException {
        File lockFile = File.createTempFile(".run", ".camel.lock", new File("."));

        System.out.printf("A new lock file was created on %s. Delete this file to stop running%n",
            lockFile.getAbsolutePath());
        lockFile.deleteOnExit();

        return lockFile;
    }
}

interface Extractor {
    void extract(String line)
}

class MatchExtractor implements Extractor {
    @Override
    public void extract(String line) {
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            String kamelet = matcher.group(3).replace(".adoc", "");
            String description = matcher.group(5);

            System.out.printf("%-35s %-45s%n", kamelet, description);
        }
    }
}



abstract class AbstractSearch {
    private String resourceLocation;
    private Pattern pattern;

    // Only used for the search subcommand
    protected AbstractSearch() {
    }

    public AbstractSearch(String resourceLocation, Pattern pattern) {
        this.resourceLocation = resourceLocation;
        this.pattern = pattern;
    }

    protected void downloadResource(File indexFile) throws Exception {
        KameletMain main = new KameletMain();
        main.start();
        CamelContext context = main.getCamelContext();

        try (GitHubResourceResolver resolver = new GitHubResourceResolver()) {
            resolver.setCamelContext(context);

            Resource resource = resolver.resolve(resourceLocation);

            if (!resource.exists()) {
                throw new Exception("The resource does not exist");
            }

            try (FileWriter w = new FileWriter(indexFile)) {
                Reader resourceReader = resource.getReader();

                resourceReader.transferTo(w);
            }
        }
    }

    /*
     * Matches the following line. Separate them into groups and pick the last
     * which contains the description:
     *
     * xref:ROOT:mariadb-sink.adoc[image:kamelets/mariadb-sink.svg[] MariaDB Sink]
     */
    private void checkForMatches(String line, String searchTerm) {
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            String description = matcher.group(5);
            String kamelet = matcher.group(3).replace(".adoc", "");

            if (description.toLowerCase().contains(searchTerm.toLowerCase())) {
                System.out.printf("%-35s %-45s%n", kamelet, description);
            }
        }
    }

//    private void printAll(String line, String searchTerm) {
//        Matcher matcher = pattern.matcher(line);
//
//        if (matcher.find()) {
//            String kamelet = matcher.group(3).replace(".adoc", "");
//            String description = matcher.group(5);
//
//            System.out.printf("%-35s %-45s%n", kamelet, description);
//        }
//    }

    private void readFileByLine(File indexFile, String searchTerm, BiConsumer<String, String> consumer) throws IOException, FileNotFoundException {
        FileReader indexFileReader = new FileReader(indexFile);
        try (BufferedReader br = new BufferedReader(indexFileReader)) {

            String line;
            do {
                line = br.readLine();

                if (line == null) {
                    break;
                }

                consumer.accept(line, searchTerm);

            } while (line != null);
        }
    }

    public abstract void printHeader();

    public void search(String searchTerm) throws Exception {
        File indexFile = new File("index");
        indexFile.deleteOnExit();

        downloadResource(indexFile);

        printHeader();
        if (!searchTerm.isEmpty()) {
            readFileByLine(indexFile, searchTerm, this::checkForMatches);
        } else {
            readFileByLine(indexFile, searchTerm, this::printAll);
        }
    }
}

@Command(name = "search", description = "Search for kameletes, components and patterns (use --help)")
class Search extends AbstractSearch implements Callable<Integer> {
    public Search() {
        super(null, null);
    }

    public void printHeader() {}

    @Override
    public Integer call() throws Exception {
        CommandLine.usage(this, System.out);

        return 0;
    }
}

@Command(name = "kamelets", description = "Search for a Kamelet in the Kamelet catalog")
class SearchKamelets extends AbstractSearch implements Callable<Integer>  {

    @Option(names = {"--search-term"}, defaultValue = "", description = "Default debug level")
    private String searchTerm;

    SearchKamelets() {
        super("github:apache:camel-kamelets:docs/modules/ROOT/nav.adoc",
                Pattern.compile("(.*):(.*):(.*)\\[(.*)\\[\\] (.*)\\]"));
    }

    @Override
    public void printHeader() {
        System.out.printf("%-35s %-45s%n", "KAMELET", "DESCRIPTION");
        System.out.printf("%-35s %-45s%n", "-------", "-----------");
    }

    @Override
    public Integer call() throws Exception {
        search(searchTerm);
        return null;
    }
}

@Command(name = "components", description = "Search for Camel Core components")
class SearchComponents extends AbstractSearch implements Callable<Integer>  {

    @Option(names = {"--search-term"}, defaultValue = "", description = "Default debug level")
    private String searchTerm;

    SearchComponents() {
        super("github:apache:camel:docs/components/modules/ROOT/nav.adoc",
                Pattern.compile("(.*):(.*)\\[(.*)\\]"));
    }

    @Override
    public void printHeader() {
        System.out.printf("%-35s %-45s%n", "COMPONENT", "DESCRIPTION");
        System.out.printf("%-35s %-45s%n", "-------", "-----------");
    }

    @Override
    public Integer call() throws Exception {
        search(searchTerm);
        return null;
    }
}

@Command(name = "CamelJBang", mixinStandardHelpOptions = true, version = "CamelJBang ${camel.jbang.version}",
        description = "A JBang-based Camel app for running Kamelets")
class CamelJBang implements Callable<Integer> {
    private static CommandLine commandLine;

    static {
        Configurator.initialize(new DefaultConfiguration());
    }

    public static void main(String... args) {
        commandLine = new CommandLine(new CamelJBang())
                .addSubcommand("run", new Run())
                .addSubcommand("search", new CommandLine(new Search())
                    .addSubcommand("kamelets", new SearchKamelets())
                    .addSubcommand("components", new SearchComponents()));

        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
    }


    @Override
    public Integer call() throws Exception {
        commandLine.execute("--help");
        return 0;
    }
}
