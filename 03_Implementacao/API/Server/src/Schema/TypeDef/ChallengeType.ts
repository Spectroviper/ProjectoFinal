import {GraphQLObjectType, GraphQLID, GraphQLString, GraphQLList, GraphQLBoolean} from 'graphql';
import { ChallengeInviteType } from './ChallengeInviteType';
import { Challenges } from '../../Entities/Challenges';
import { CustomDate } from '../CustomTypes/Date';
import { ChallengeAchievementType } from './ChallengeAchievementType';
import { UserChallengeType } from './UserChallengeType';

export const ChallengeType: GraphQLObjectType<Challenges> = new GraphQLObjectType({
    name: "Challenge",
    fields: () => ({
        ChallengeId: { type: GraphQLID },
        ChallengeName: { type: GraphQLString },
        Type: { type: GraphQLString },
        IsStarted: { type: GraphQLBoolean },
        StartDate: { type: CustomDate},
        EndDate: { type: CustomDate},
        Image: { type: GraphQLString },
        CreatedBy: { type: GraphQLString },
        UserChallenges: { type: new GraphQLList(UserChallengeType), },
        ChallengeAchievements: { type: new GraphQLList(ChallengeAchievementType) },
        ChallengeInvites: { type: new GraphQLList(ChallengeInviteType)}
    })
})