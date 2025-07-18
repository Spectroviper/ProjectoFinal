import { GraphQLFloat, GraphQLID, GraphQLObjectType } from "graphql";
import { UserGames } from "../../Entities/UserGames";
import { GameType } from "./GameType";
import { UserType } from "./UserType";

export const UserGameType: GraphQLObjectType<UserGames> = new GraphQLObjectType({
    name: "UserGame",
    fields: () => ({
        UserGameId: { type: GraphQLID },
        AverageCompletion: { type: GraphQLFloat },
        User: { type: UserType },
        Game: { type: GameType }
    })
})