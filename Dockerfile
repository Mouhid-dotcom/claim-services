FROM openjdk
WORKDIR app
COPY . .
CMD cd target;java -jar claim-services-0.0.1-SNAPSHOT.jar
EXPOSE 53284