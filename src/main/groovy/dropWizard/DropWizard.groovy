package dropWizard

import org.vertx.groovy.platform.Verticle

import java.nio.file.Path
import java.nio.file.Paths

class DropWizard extends Verticle {

    def start() {
        def proc = 'ls -al'.execute()
        proc.waitFor()
        println "return code: ${proc.exitValue()}"
        println "stderr: ${proc.err.text}"
        println "stdout: ${proc.in.text}"

        // register directory and process its events
        boolean recursive = true
        Path dir = Paths.get("/Users/sumnulu/Projects/dropWizard")
        def WatchDir = new WatchDir(dir, recursive).processEvents()

    }


}
