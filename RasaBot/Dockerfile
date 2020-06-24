# Extend the official Rasa SDK image
FROM rasa/rasa-sdk:1.10.0

# Use subdirectory as working directory
WORKDIR /app

# Copy any additional custom requirements, if necessary (uncomment next line)
# COPY actions/requirements-actions.txt ./

# Change back to root user to install dependencies
USER root

RUN mkdir -p /usr/share/man/man1

# define the port number the container should expose
EXPOSE 5055

RUN apt-get update && \
    apt-get install -y  default-jre

# Install extra requirements for actions code, if necessary (uncomment next line)
RUN pip install duckling==1.8.0 

# Copy actions folder to working directory
COPY ./actions /app/actions

# By best practices, don't run the code with root user
USER 1001