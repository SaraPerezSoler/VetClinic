# VetClinic
This repository contains the implementation of three chatbots for a veterinary clinic, using different technologies: Dialogflow, Rasa and FlowXO. The chatbots allow consulting the opening hours and making appointments for pets. The repository has three folders:
* DialogflowBot contains a zip file with json files to restore the chatbot in Dialogflow.
* RasaBot contains the rasa files (python, markdown, YAML) to run the chatbot with Rasa.
* Back-end contains a Java program with a Rest API to receive requests from the chatbots to query and update a common database.

# Bot access
The chatbots for the veterinary clinic are accessible via Telegram at the following links:
 * Dialogflow development: https://t.me/VetClinicDialogflowBot
 * Rasa development: https://t.me/VetClinicRasaBot
 * FlowXO development: https://t.me/VetClinicFlowxoBot

You can access the links from a mobile device using the Telegram app, or from a desktop computer using either the Telegram web application or the Telegram desktop application. Having a Telegram account is also required. 

# Bot operation

The chatbot has three main functionalities:
* Greet: If you greet the chatbot, he answers by introducing himself.
* Report opening hours: if you ask the chatbot about the clinic opening hours, he informs about the clinic working hours.
* Make an appointment: If you request to fix an appointment, the chatbot calls an external service (Back-end folder) to search for available slots and save the appointment in the database.

