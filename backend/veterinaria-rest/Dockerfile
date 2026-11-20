FROM tomcat:10.1-jdk17-temurin
LABEL authors="David"

# Limpiar aplicaciones predeterminadas de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el WAR asignándole el context path /api/v1 mediante #
COPY target/*.war "/usr/local/tomcat/webapps/api#v1.war"

# El puerto interno de Tomcat dentro del contenedor sigue siendo 8080
EXPOSE 8080

CMD ["catalina.sh", "run"]