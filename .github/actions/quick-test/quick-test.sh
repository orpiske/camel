#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

MVN_DEFAULT_OPTS="-Djava.security.properties=$HOME/Sync/Data/util/devel/custom.java.security -Dmaven.compiler.fork=true -Dsurefire.rerunFailingTestsCount=2"
MVN_OPTS=${MVN_OPTS:-$MVN_DEFAULT_OPTS}
errors=0
basedir=$(pwd)
testDate=$(date '+%Y-%m-%d-%H%M%S')
logDir=${basedir}/automated-build-log/${testDate}
testHost=$(hostname)

echo $MVN_OPTS

function notifySuccess() {
	local component=$1
	local total=$2
	local current=$3

	echo "${component} test completed successfully: ${current} verified / ${errors} errored"  | tee ${logDir}/test.log
	# notify-pushover "${component} test completed successfully: ${current} verified / ${errors} errored @ ${testHost}"
}

function notifyError() {
	local component=$1
	local total=$2
	local current=$3

	echo "Failed ${component} test: ${current} verified / ${errors} errored"  | tee ${logDir}/test.log
	# notify-pushover "Failed ${component}: ${current} verified / ${errors} errored @ ${testHost}"
}


function runTest() {
	local component=$1
	local total=$2
	local current=$3

	echo "############################################################"  | tee ${logDir}/test.log
	echo "Testing component ${current} of ${total}: ${component}"  | tee ${logDir}/test.log
	echo "############################################################"  | tee ${logDir}/test.log
	echo ""  | tee ${logDir}/test.log

	echo mvn -Psourcecheck ${MVN_OPTS} verify 2>&1 >> "${logDir}/${component/\//-}.log"
	if [[ $? -ne 0 ]] ; then
		((errors++))
		notifyError "${component} test"  "${total}" "${current}" "${errors}"
	else
		notifySuccess "${component}" "${total}" "${current}" "${errors}"
	fi
}


function componentTest() {
	local component=$1
	local total=$2
	local current=$3

	cd ${basedir}/${component}
	runTest "${component}" "${total}" "${current}"
}

function main() {
	local current=0
	local startCommit=$1
	local endCommit=$2

	local components=$(git show ${startCommit} ${endCommit} --name-only --pretty=format:"" | grep components | cut -d /  -f 1-2 | uniq | sort)
	local total=$(echo "${components}" | wc -l)


	# notify-pushover "Starting to test ${total} components"
	mkdir -p ${logDir}
	echo "Will test the following ${total} components:" | tee ${logDir}/test.log
	echo "${components}" | tee ${logDir}/test.log

	current=0
	mkdir -p "${logDir}"
	for component in $(echo $components) ; do
		((current++))
		componentTest "${component}" "${total}" "${current}"
	done

	echo "Finished verification: ${total} verified / ${errors} errored"  | tee ${logDir}/test.log
#	notify-pushover "Finished verification: ${total} verified / ${errors} errored @ ${testHost}"
}


pwd
main "$@"

