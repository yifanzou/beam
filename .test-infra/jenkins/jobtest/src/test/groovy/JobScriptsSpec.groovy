import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement
import org.junit.ClassRule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


class JobScriptsSpecification extends Specification {
    static scriptExclusion = ['comon_job_properties.groovy',
            'job_00_seed.groovy',
            'job_seed_standalone.groovy',
                              'job_beam_PreCommit_Pipeline.groovy',
    ]

    @Shared
    @ClassRule
    JenkinsRule jenkinsRule = new JenkinsRule()

    @Unroll
    def 'test script #file.name'(File file) {
        given:
        def jobManagement = new JenkinsJobManagement(System.out, [:], new File('.'))

        when:
        new DslScriptLoader(jobManagement).runScript(file.text)

        then:
        noExceptionThrown()

        where:
        file << jobFiles
    }

    static List<File> getJobFiles() {
        List<File> files = []
        new File('../').eachFileRecurse {
            if (it.name.endsWith('.groovy') && it.name.startsWith("job_") && !scriptExclusion.contains(it.name)) {
                files << it
            }
        }
        files
    }
}