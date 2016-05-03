FROM glassfish

MAINTAINER  Space Ghost

# Installs gradle
ENV GRADLE_DIR /gradle
RUN mkdir -p ${GRADLE_DIR}
WORKDIR ${GRADLE_DIR}
RUN wget https://services.gradle.org/distributions/gradle-2.3-bin.zip \
        unzip gradle-2.3-bin.zip

ENV         JAVA_HOME         /usr/lib/jvm/java-8-openjdk-amd64
ENV         GRADLE_HOME    /gradle/gradle-2.3-bin
ENV         GLASSFISH_HOME    /usr/local/glassfish4
ENV         PATH              $PATH:$JAVA_HOME/bin:$GLASSFISH_HOME/bin:$GRADLE_HOME/bin

# Builds project
ENV PROJECT_DIR /kafetaka
RUN mkdir -p ${PROJECT_DIR}
COPY ./ ${PROJECT_DIR}
WORKDIR ${PROJECT_DIR}
RUN gradle war

# Deploys project to glassfish
WORKDIR ${GLASSFISH_HOME}
RUN /bin/bash -c 'mv ${PROJECT_DIR}/build/libs/*.war ${GRADLE_HOME}/domains/domain1/autodeploy/'

CMD         asadmin start-domain --verbose