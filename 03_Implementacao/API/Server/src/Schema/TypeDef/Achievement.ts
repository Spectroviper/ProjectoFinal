import {GraphQLObjectType, GraphQLID, GraphQLString, GraphQLList, GraphQLInt} from 'graphql';
import { Achievements } from '../../Entities/Achievements';
import { CustomImage } from '../CustomTypes/Image';
import { GameType } from './Game';

export const AchievementType: GraphQLObjectType<Achievements> = new GraphQLObjectType({
    name: "Achievement",
    fields: () => ({
        id: { type: GraphQLID },
        AchievementName: { type: GraphQLString },
        About: { type: GraphQLString },
        RetroPoints: { type: GraphQLInt },
        TotalCollectable: { type: GraphQLInt },
        Image: { type: CustomImage },
        Game: { type: GameType }
    })
})