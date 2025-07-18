import { GraphQLBoolean, GraphQLFloat, GraphQLID, GraphQLObjectType } from "graphql";
import { UserChallenges } from "../../Entities/UserChallenges";
import { ChallengeType } from "./ChallengeType";
import { UserType } from "./UserType";

export const UserChallengeType: GraphQLObjectType<UserChallenges> = new GraphQLObjectType({
    name: "UserChallenge",
    fields: () => ({
        UserChallengeId: { type: GraphQLID },
        IsLeader: { type: GraphQLBoolean },
        AverageCompletion: { type: GraphQLFloat },
        User: { type: UserType },
        Challenge: { type: ChallengeType }
    })
})