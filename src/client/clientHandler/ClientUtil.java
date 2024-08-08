package client.clientHandler;


public class ClientUtil {
    public static SquadState getStateWithString(String state) {
        if (state.equals("NO_SQUAD")) return SquadState.NO_SQUAD;
        if (state.equals("LEADER"))  return SquadState.LEADER;
        return SquadState.MEMBER;
    }
    public static BattleStatus getBattleStateWithString(String state) {
        if (state.equals("YES")) return BattleStatus.YES;
        return BattleStatus.NO;
    }
}
