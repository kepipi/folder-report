FROM openjdk:8
ENV APPLICATION='folder-report'
VOLUME /tmp

ADD folder-report-4.1.0.jar folder-report.jar

RUN bash -c 'touch /folder-report.jar'
RUN ln -sf /usr/share/zoneinfo/America/New_York /etc/localtime
RUN echo 'America/New_York' >/etc/timezone

EXPOSE 11101

ENTRYPOINT ["java", "-jar", "/folder-report.jar"]