# URL SHORTENER + FORWARDING API
This repository contains a Dockerfile used with the docker-compose.yml file in order to build and launch the 
docker images. One container runs the redis instance and the other runs the API itself.
The API provides the ability to POST a URL and receive a response with the shortened link.
If a user follows this link through the browser, the API will get the full URL from the redis key/value
store and forward the user to the corresponding website.
The API utilises the MurmurHash to reduce the size of the URL. The hash is then appended to the base URL
of the API creating a shortened URL. 

## Setup
   - Navigate to the directory the repository was cloned into.
   - Run `docker-compose up` 
        (use `docker-compose up -d` to detach the container from the terminal)
   - The redis instance and API should now be running and usable through Curl or Postman.
    
## Usage
   - Using Curl
     ```
     curl --location --request POST 'http://localhost:8080/' \
          --header 'Content-Type: text/plain' \
          --data-raw 'https://www.neueda.com/covid-response'
     ```
          
   - Using Postman
        - Create a POST request with request URL : http://localhost:8080/
        - In the body of the POST request, select raw input and input a URL. e.g. https://www.neueda.com/covid-response
        
   - Both of these methods will return a URL similar to this : http://localhost:8080/87e1161d
        - If this URL is followed, the user will be redirected to the corresponding website.

## Version Information
   - Docker Version : `19.03.13`
   - Java Version : `openjdk 11.0.9`
   - Redis Version : `6.0.8`

### Notes
   - `host` network mode is used in the docker-compose.yml file due to a known problem between Docker and local firewalls causing "No Route to Host" exceptions.
        - This is a common problem faced by many people with local config changes required to make it work
            - e.g. https://stackoverflow.com/questions/40214617/docker-no-route-to-host
        - To make this easier to run locally, the network_mode host is used.
   - A base version of `redis` is used within the docker-compose.yml file as there is no customization required.
