import {GraphQLObjectType, GraphQLID, GraphQLString, GraphQLList} from 'graphql';
import { Games } from '../../Entities/Games';
import { CustomDate } from '../CustomTypes/Date';
import { AchievementType } from './AchievementType';
import { UserGameType } from './UserGameType';

export const GameType: GraphQLObjectType<Games> = new GraphQLObjectType({
    name: "Game",
    fields: () => ({
        GameId: { type: GraphQLID },
        GameName: { type: GraphQLString },
        About: { type: GraphQLString },
        Console: { type: GraphQLString },
        Developer: { type: GraphQLString },
        Publisher: { type: GraphQLString },
        Genre: { type: GraphQLString },
        ReleaseDate: { type: CustomDate},
        Image: { type: GraphQLString },
        CreatedBy: { type: GraphQLString },
        UserGames: { type: new GraphQLList(UserGameType) },
        Achievements: { type: new GraphQLList(AchievementType)}
    })
})