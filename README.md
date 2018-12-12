# JavaChaosNetClient [![Build Status](https://travis-ci.org/suppergerrie2/JavaChaosNetClient.svg?branch=master)](https://travis-ci.org/suppergerrie2/JavaChaosNetClient) [![](https://jitpack.io/v/suppergerrie2/JavaChaosNetClient.svg)](https://jitpack.io/#suppergerrie2/JavaChaosNetClient)

Client for ChaosNet written in java (Written mostly as an experiment for me).

An example to login, create a trainingroom and get the trainingrooms:

```java

ChaosNetClient client = new ChaosNetClient();

//Authorize with a username and password. Pass true if the refreshtoken should be saved (Not implemented yet)
client.Authorize(getUsername(), getPassword(), false);

//Create a trainingroom with name "ExampleTrainingRoom" and in the "ExampleNamespace" namespace.
client.createTrainingRoom(new TrainingRoom("ExampleTrainingRoom", "ExampleNamespace");

//The results are in an array of trainingrooms, do with it what you want!
TrainingRoom[] result = client.getTrainingRooms();

```

# Installation

For installation instructions go to [jitpack](https://jitpack.io/#suppergerrie2/JavaChaosNetClient) and select the version you want.

# TODO:

- [x] Login
- [ ] Use refresh token
- [ ] Ability to save login token
- [ ] Manage session
- [ ] Manage networks
