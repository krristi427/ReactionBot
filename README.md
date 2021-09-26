# ReactionBot

![](https://img.shields.io/static/v1?label=Discord&message=ReactionBot&color=5865F2&style=for-the-badge&logo=discord)
![Docker Image Size (latest by date)](https://img.shields.io/docker/image-size/krristi1234/reactionbot?color=%23ffa352&logo=docker&style=for-the-badge)

![GitHub Workflow Status](https://img.shields.io/github/workflow/status/krristi427/ReactionBot/Bot%20CI?style=for-the-badge)
![GitHub issues](https://img.shields.io/github/issues/krristi427/ReactionBot?style=for-the-badge)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/krristi427/ReactionBot?style=for-the-badge)

![GitHub](https://img.shields.io/github/license/krristi427/ReactionBot?style=for-the-badge)

## About The Project

This is a simple Bot built for distributing roles to users in a Discord Server (also known as Guilds). The general Idea is that someone can use a slash command,
provide the necessary parameters listed below and hit enter. The Bot will then get the available roles in the server, 
send a fancy message as a reply and the users can select their desired role from a dropdown menu. 
This guarantees that a valid role in the server is selected and avoids frequent errors in old, message-based commands caused by misspelled words. 
In compliance with Human-Computer-Interaction guidelines, the Bot also send a direct feedback in a private channel, letting the 
user know that something has changed and greeting him/her for the new role. 

The command is `/arr` and it has the following parameters: 

- **title**: 

   As the name suggests, you must provide a title. It will be printed in bold and must be something to take the user's attention. 
   
- **text**: 

   A descriptive text of what the message is about, what motivates it and what should the users do...you know the stuff everyone learns in a typical writing class :wink:
   
- **footer**: 

   One day i was feeling generous and decided to give you an optional parameter to set the text that will appear at the bottom of your message. You can leave it empty though
   and just enjoy what the Bot has to say :wink:
   
<!-- DEMOOOO -->
   
### Built With: 

The main frameworks, libraries, APIs and stuff are listed here. You don't need to have an understanding of everything, as many of them have their own support-channels 

- [JDA](https://github.com/DV8FromTheWorld/JDA) 

   The JavaDiscordAPI is the endpoint that connects this application to Discord. The have a very active and growing community on Discord, where they are help each
   other provide ways of improving one's code. If you use guild-related commands, the results will be instantly visible in the client, else (using a JDA-instance)
   it might take up to the well-known hour. Just please for the love of everything you consider holy :angel: do not start with one
   and then switch to the other. It will disorient the client and your old commands will probably still be there.
   
- [Spring Boot](https://spring.io/projects/spring-boot)

   One of the most popular Java-Frameworks, Spring is used to easily create Web-Applications and reduce the boilerplate code every project has. Here I've made use of
   Spring Boot's famous Annotations. If you are unsure of what they do/when to use what, I suggest you start by understanding what an Application Bean is...
   it will come up so often, you'll probably google it yourself :wink: In Addition, layered JARs were also a crucial part at improving the performance overall. 
   
- [Docker](https://www.docker.com/)

   Docker takes away repetitive configuration tasks and is used throughout the development lifecycle for easy application development. By utilizing Docker's smart
   cache system paired with layered JARs, the performance increased, as now not every layer is built anew, but most of them are left untouched,
   simply because nobody needed to change anything. It goes without saying that introducing breaking changes :boom: will increase the build time, 
   however simple development changes strive on this feature. 
   
## Getting Started

Here is what you should do to get the ReactionBot on your Server: 

### Prerequisites: 

- [Java 16](https://www.oracle.com/java/technologies/downloads/#java16)
- [Docker](https://www.docker.com/)
- [Git](https://git-scm.com/)
- [Gradle](https://gradle.org/)

   You basically need Java :coffee: if you're going to run a java application and docker :whale2: for the container.
   Gradle :elephant: is there because i'm just too lazy to find out how to provide the token to the jar from the command line...but hey that would make a wonderful pull-request :wink:
   
 #### You can skip Step 1-3 if you're a smart-a** that knows how to create discord applications and invite them to your server. 

1. **Create a Discord Application.**

   Visit [this page](https://discord.com/developers/applications), login with your credentials and hit the button `New Application`...not too hard though: discord is sometimes sensitive.
   You can give your Application a Name (this is what will be shown in the server) and an icon to make it more user-friendly. 
   
2. **Add a Bot**

   At the menu on the left, under `Bot`, click `Add a Bot`. This is when Discord knows your application is indeed a Bot and will handle it accordingly. 
   Your Bot also needs some Permissions in order to function properly. The ones it can not function without are: `Manage Roles` and `Use Slash Commands`. Now would be a 
   good idea to copy the Token, as we will need it later. 
   
3. **Authentify and Invite**

   Again, on the left, click `OAuth` and give your Bot the `bot` and `applications.commands` scope. In Addition to that, provide the same Permissions as in Step 2. 
   An invite Link is generated in the scope. Copy-paste it in your browser and select the server you'd like your bot to join to. 
   
4. **Get The Code**

   Clone the repository with 
   ```sh
   git clone https://github.com/krristi427/ReactionBot.git your_local_directory
   ```
   
5. **Final Preparations**
   
   Navigate to ./Dockerfile and paste the Token of your Application from Step 2. Finally, all you need to do is run a `gradle build` in the main directory and you should be all set. 
   
6. **Start the hell out of it**

   Like every other legendary application, your Bot must also start by waking up the docker whale :whale2: in a detached mode  
   ```sh
   docker-compose up -d --build
   ```
   at the command-line. 
   
If you happen to run into any issues during these steps, feel free to create a [New Issue](https://github.com/krristi427/ReactionBot/issues) and tell me all about it. 
I will be working on a new polling feature that shows the results of polls visually, so I will keep an eye out for Issues :wink:
   
   
<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to be! Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### A small heads up: 

While developing, you might see some weird `NoSuchFileException` or `FileNotFoundException` for a specific .json. 
Have you tried changing the path as instructed in the big comment-block at the beginning of the file? If that doesn't work, 
try invalidating the cache and restarting you IDE or if you'd like a more brutal solution: just restart your PC 



   
