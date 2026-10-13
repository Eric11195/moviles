package com.example.game.testing.Systems;


public class LevelGeneratorParams{
    //Px speed per second. The obstacles move that much towards the left of the screen
    public float levelInitialSpeed;
    public float levelDurationInSeconds;
    public int maxNumberOfSimultaneousObstacles;
    /**
     * What are the chances of having an explosive obstacle creation
     * Moment in which the obstacle creator receives a huge boosts on tokens.
     */
    public double obstacleExplosiveCreationChance;
    /**
     * When obstacleExplosiveCreationChance happens.
     * How many token obstacles to add to the obstacle creator in that second. Float number.
     */
    public float obstacleExplosiveCreationBoost;
    /**
     * each second this amount of obstacle are created by default
     */
    public float obstacleLinearCreationPerSecond;
}