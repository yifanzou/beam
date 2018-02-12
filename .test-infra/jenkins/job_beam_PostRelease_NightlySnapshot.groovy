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

// This verifies the nightly snapshot build.
// From https://repository.apache.org/content/groups/snapshots/org/apache/beam.
job('beam_PostRelease_NightlySnapshot') {
  description('Runs post release verification of the nightly snapshot.')

  // Execute concurrent builds if necessary.
  concurrentBuild()

  // Set common parameters.
  common_job_properties.setTopLevelMainJobProperties(delegate)

  parameters {
    stringParam('snapshot_version',
                '',
                'Version of the repository snapshot to install')
    stringParam('snapshot_url',
                '',
                'Repository URL to install from')
  }

  // This is a post-commit job that runs once per day, not for every push.
  common_job_properties.setPostCommit(
      delegate,
      '0 11 * * *',
      false)


  // Allows triggering this build against pull requests.
  common_job_properties.enablePhraseTriggeringFromPullRequest(
      delegate,
      //'./gradlew :release:runQuickstartsJava',
      './gradlew :release:runQuickstartsPython',
      'Run Dataflow PostRelease')

  steps {
    // Run a quickstart from https://beam.apache.org/get-started/quickstart-java
    gradle {
      rootBuildScriptDir(common_job_properties.checkoutDir)
      //tasks(':release:runQuickstartsJava')
      tasks('release:runQuickstartsPython')
      switches('-Pver=$snapshot_version -Prepourl=$snapshot_url')
    }
  }
}
