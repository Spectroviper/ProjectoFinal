import {GraphQLObjectType, GraphQLID, GraphQLString, GraphQLInt, GraphQLList} from 'graphql';
import { Users } from '../../Entities/Users';
import { CustomDate } from '../CustomTypes/Date';
import { ChallengeInviteType } from './ChallengeInviteType';
import { UserAchievementType } from './UserAchievementType';
import { UserChallengeAchievementType } from './UserChallengeAchievementType';
import { UserChallengeType } from './UserChallengeType';
import { UserGameType } from './UserGameType';
import { FriendType } from './FriendType';

export const UserType: GraphQLObjectType<Users> = new GraphQLObjectType({
    name: "User",
    description: "A User in the Application",
    fields: () => ({
        UserId: { type: GraphQLID },
        UserName: { type: GraphQLString },
        Email: { type: GraphQLString },
        Biography: { type: GraphQLString },
        MemberSince: { type: CustomDate },
        LastLogin: { type: CustomDate },
        TotalPoints: { type: GraphQLInt },
        AverageCompletion: {  type: GraphQLInt },
        Image: { type: GraphQLString },
        UserGames: { type: new GraphQLList(UserGameType) },
        UserAchievements: { type: new GraphQLList(UserAchievementType),
            resolve: async (parent: Users) => {
                return parent.UserAchievements || [];
            },
        },
        UserChallengeAchievements: { type: new GraphQLList(UserChallengeAchievementType), 
            resolve: async (parent: Users) => {
                return parent.UserChallengeAchievements || [];
            },
        },
        UserChallenges: { type: new GraphQLList(UserChallengeType), },
        ChallengeInvites: { type: new GraphQLList(ChallengeInviteType) },
        FriendSenders: { type: new GraphQLList(FriendType) },
        FriendReceivers: { type: new GraphQLList(FriendType) }
    })
})