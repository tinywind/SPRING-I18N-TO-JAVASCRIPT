/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Jeon JaeHyeong
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.tinywind.springi18nconverter.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.tinywind.springi18nconverter.Launcher;
import org.tinywind.springi18nconverter.jaxb.Configuration;
import org.tinywind.springi18nconverter.jaxb.Source;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;
import static org.apache.maven.plugins.annotations.ResolutionScope.TEST;

/**
 * @author tinywind
 */
public class Plugin extends AbstractMojo {
    /**
     * The Maven project.
     */
    @Parameter(property = "project", required = true, readonly = true)
    protected MavenProject project;

    /**
     * Whether to skip the execution of the Maven Plugin for this module.
     */
    @Parameter
    protected boolean skip;

    @Parameter
    protected List<Source> sources;

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("Skip SPRING-I18N-CONVERTER");
            return;
        }

        final Configuration configuration = new Configuration();
        configuration.setSources(sources);

        final StringWriter writer = new StringWriter();
        JAXB.marshal(configuration, writer);

        getLog().debug("Using this configuration:\n" + writer.toString());

        try {
            Launcher.generate(configuration);
        } catch (IOException e) {
            e.printStackTrace();
            getLog().error(e.getMessage());
            if (e.getCause() != null)
                getLog().error("  Cause: " + e.getCause().getMessage());
            return;
        }

        getLog().info("Complete SPRING-I18N-CONVERTER");
    }
}
