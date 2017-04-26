package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/*
      This method recieves calls and has to be implementet for every entity that must be updated constantly.
      The GameData Class contains all the events that has to be processed through a list.
      The World class contains all entity's that's in the world.
     
*/

public interface IEntityProcessingService {

    void process(GameData gameData, World world);
}

