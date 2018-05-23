/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import common_job_properties

job('beam_Dependency_Check') {
  description('Runs Beam dependency check.')

  // Set common parameters.
  common_job_properties.setTopLevelMainJobProperties(delegate)

  // Allows triggering this build against pull requests.
  common_job_properties.enablePhraseTriggeringFromPullRequest(
    delegate,
    'Beam Dependency Check',
    'Run Dependency Check')

  def emailContents
  steps {
    // gradle task :dependencyUpdates will do Java dependency version check
    // ./gradlew :dependencyUpdates -Drevision=release -DreportfileName=javaDependencyReport
    gradle {
      rootBuildScriptDir(common_job_properties.checkoutDir)
      tasks(':dependencyUpdates')
      common_job_properties.setGradleSwitches(delegate)
      switches('-Drevision=release')
    }

    // The shell script "run_dependency_check" will do version check for Python SDK.
    shell('cd ' + common_job_properties.checkoutDir +
      ' && bash sdks/python/run_dependency_check.sh')

    groovyCommand('emailContents = common_job_properties.generateDependencyReport()') {
        classpath('./common_job_properties.groovy')
    }
  }

  publishers {
  extendedEmail {
    triggers {
      always {
        recipientList('yifanzou@google.com')
        contentType('text/plain')
        subject('Beam Dependency Check In Jenkins')
        content(emailContents)
        }
      }
    }
  }
}
