FROM tomcat:10.1-jdk17-temurin
LABEL authors="David"

# Limpiar aplicaciones predeterminadas de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el WAR como ROOT para que la URL sea la misma que desde IntelliJ:
# http://localhost:8095/services/mascotas?wsdl
COPY target/*.war "/usr/local/tomcat/webapps/ROOT.war"

# El puerto interno de Tomcat dentro del contenedor sigue siendo 8080
EXPOSE 8080

CMD ["catalina.sh", "run"]