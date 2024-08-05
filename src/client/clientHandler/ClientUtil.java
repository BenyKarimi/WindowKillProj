package client.clientHandler;

import server.models.SquadState;

public class ClientUtil {
    public static SquadState getStateWithString(String state) {
        if (state.equals("NO_SQUAD")) return server.models.SquadState.NO_SQUAD;
        if (state.equals("LEADER"))  return server.models.SquadState.LEADER;
        return SquadState.MEMBER;
    }
}
