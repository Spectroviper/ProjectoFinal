import {GraphQLObjectType, GraphQLID, GraphQLString, GraphQLList} from 'graphql';
import { Games } from '../../Entities/Games';
import { CustomDate } from '../CustomTypes/Date';
import { CustomImage } from '../CustomTypes/Image';
import { AchievementType } from './Achievement';
import { PersonType } from './Person';

export const GameType: GraphQLObjectType<Games> = new GraphQLObjectType({
    name: "Game",
    fields: () => ({
        id: { type: GraphQLID },
        GameName: { type: GraphQLString },
        About: { type: GraphQLString },
        Console: { type: GraphQLString },
        Developer: { type: GraphQLString },
        Publisher: { type: GraphQLString },
        Genre: { type: GraphQLString },
        ReleaseDate: { type: CustomDate},
        Image: { type: CustomImage },
        Players: { type: new GraphQLList(PersonType) },
        Achievements: { type: new GraphQLList(AchievementType)}
    })
})