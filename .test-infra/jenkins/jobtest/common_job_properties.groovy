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

// Contains functions that help build Jenkins projects. Functions typically set
// common properties that are shared among all Jenkins projects.
// Code in this directory should conform to the Groovy style guide.
//  http://groovy-lang.org/style-guide.html
class common_job_properties {

  static String checkoutDir = 'src'

  static void setSCM(def context, String repositoryName) {}

  // Sets common top-level job properties for website repository jobs.
  static void setTopLevelWebsiteJobProperties(def context,
                                              String branch = 'asf-site') {}

  // Sets common top-level job properties for main repository jobs.
  static void setTopLevelMainJobProperties(def context,
                                           String branch = 'master',
                                           int timeout = 100,
                                           String jenkinsExecutorLabel = 'beam') {}

  // Sets common top-level job properties. Accessed through one of the above
  // methods to protect jobs from internal details of param defaults.
  private static void setTopLevelJobProperties(def context,
                                               String repositoryName,
                                               String defaultBranch,
                                               String jenkinsExecutorLabel,
                                               int defaultTimeout) {}

  // Sets the pull request build trigger. Accessed through precommit methods
  // below to insulate callers from internal parameter defaults.
  private static void setPullRequestBuildTrigger(context,
                                                 String commitStatusContext,
                                                 String prTriggerPhrase = '',
                                                 boolean onlyTriggerPhraseToggle = true,
                                                 String successComment = '--none--') {}

  // Sets common config for Maven jobs.
  static void setMavenConfig(context, String mavenInstallation='Maven 3.5.2') {}

  // Sets common config for PreCommit jobs.
  static void setPreCommit(context,
                           String commitStatusName,
                           String prTriggerPhrase = '',
                           String successComment = '--none--') {}

  // Enable triggering postcommit runs against pull requests. Users can comment the trigger phrase
  // specified in the postcommit job and have the job run against their PR to run
  // tests not in the presubmit suite for additional confidence.
  static void enablePhraseTriggeringFromPullRequest(context,
                                                    String commitStatusName,
                                                    String prTriggerPhrase) {}

  // Sets common config for PostCommit jobs.
  static void setPostCommit(context,
                            String buildSchedule = '0 */6 * * *',
                            boolean triggerEveryPush = true,
                            String notifyAddress = 'commits@beam.apache.org',
                            boolean emailIndividuals = true) {}

  static def mapToArgString(LinkedHashMap<String, String> inputArgs) {}

  // Configures the argument list for performance tests, adding the standard
  // performance build job arguments.
  private static def genPerformanceArgs(def argMap) {}

  // Adds the standard performance build job steps.
  static def buildPerformanceTest(def context, def argMap) {}

  /**
   * Sets properties for all jobs which are run by a pipeline top-level (maven) job.
   * @param context    The delegate from the top level of a MavenJob.
   * @param jobTimeout How long (in minutes) to wait for the job to finish.
   * @param descriptor A short string identifying the job, e.g. "Java Unit Test".
   */
  static def setPipelineJobProperties(def context, int jobTimeout, String descriptor) {}

  /**
   * Sets job properties common to pipeline jobs which are responsible for being the root of a
   * build tree. Downstream jobs should pull artifacts from these jobs.
   * @param context The delegate from the top level of a MavenJob.
   */
  static def setPipelineBuildJobProperties(def context) {}

  /**
   * Sets common job parameters for jobs which consume artifacts built for them by an upstream job.
   * @param context The delegate from the top level of a MavenJob.
   * @param jobName The job from which to copy artifacts.
   */
  static def setPipelineDownstreamJobProperties(def context, String jobName) {}
}
