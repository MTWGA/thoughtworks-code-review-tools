package com.github.tcpgnl.thoughtworkscodereviewtools.services

import com.github.tcpgnl.thoughtworkscodereviewtools.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
