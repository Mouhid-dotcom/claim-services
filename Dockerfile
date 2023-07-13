FROM openjdk
WORKDIR /usr/src/myapp
COPY . /usr/src/myapp
CMD [ "java","-jar","claim-services-0.0.1-SNAPSHOT.jar" ] 
EXPOSE 53284