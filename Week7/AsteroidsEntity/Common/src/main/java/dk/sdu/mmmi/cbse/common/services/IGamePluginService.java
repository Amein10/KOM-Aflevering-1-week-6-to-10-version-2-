package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/*
void start:
      When this method starts, the first thing it should do is install the component. 
      The GameData Class contains all the events that has to be processed through a list.
      The World class contains all entity's that's in the world.

void stop:

      When this method stops, the last thing it should do is removing the component.
      The GameData Class contains all the events that has to be processed through a list.
      The World class contains all entity's that's in the world.
 */
public interface IGamePluginService {

    void start(GameData gameData, World world);

    void stop(GameData gameData, World world);
}
