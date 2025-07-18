import {GraphQLObjectType, GraphQLID, GraphQLString, GraphQLList, GraphQLInt} from 'graphql';
import { Achievements } from '../../Entities/Achievements';
import { GameType } from './GameType';
import { UserAchievementType } from './UserAchievementType';
import { ChallengeAchievementType } from './ChallengeAchievementType';

export const AchievementType: GraphQLObjectType<Achievements> = new GraphQLObjectType({
    name: "Achievement",
    fields: () => ({
        AchievementId: { type: GraphQLID },
        AchievementName: { type: GraphQLString },
        About: { type: GraphQLString },
        TotalCollectable: { type: GraphQLInt },
        Image: { type: GraphQLString },
        CreatedBy: { type: GraphQLString },
        Game: { type: GameType },
        ChallengeAchievements: { type: new GraphQLList(ChallengeAchievementType) },
        UserAchievements: { type: new GraphQLList(UserAchievementType) }
    })
})