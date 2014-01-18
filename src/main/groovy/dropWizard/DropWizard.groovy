package dropWizard

import org.vertx.groovy.platform.Verticle

import java.nio.file.Path
import java.nio.file.Paths

class DropWizard extends Verticle {

    /**
     * Allowed file extensions to be executed
     */
    final validScriptExtensions = ['groovy', 'sh', 'bat']


    /**
     * Start Vertical
     */
    def start() {

        // register directory and process its events
        boolean recursive = true
        Path dir = Paths.get("/Users/sumnulu/Projects/dropWizard/out/test")

        def watchDir = new WatchDir(dir, recursive)

        watchDir.processEvents { Path path, String eventType ->
            println "type: $eventType, file: $path"

            def file = path.toFile()

            if (file.isFile()) {
                //So this is not a directory

                def lowerCaseFileName = path.fileName.toString().toLowerCase()
                boolean isScript = validScriptExtensions.any { lowerCaseFileName.endsWith(it) }

                if (isScript) {
                    //This is a file and a script

                    //Check if we have execute permission if not set permission i.e. chmod file +x
                    if (isScript && !file.canExecute()) file.setExecutable(true)

                    //Now we have executable file
                    execute(path)
                }
            }
        }

    }


    /**
     * Execute file and write output
     * i.e:
     * ./my_script.sh -> ./my_script.sh.out
     *                   ./my_script.sh.err
     *
     * @param executeAbleFilePath Can be String or Path or File etc.
     */
    static execute(def executeAbleFilePath) {
        def file = executeAbleFilePath as String

        def proc = file.execute()

        //BLOCK!
        proc.waitFor()

        //Write Standard output
        new File(file + '.out').write proc.in.text

        //Write Error output
        new File(file + '.err').write proc.err.text

    }


}
