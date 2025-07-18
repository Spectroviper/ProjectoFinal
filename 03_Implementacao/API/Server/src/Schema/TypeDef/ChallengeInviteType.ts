import { GraphQLBoolean, GraphQLID, GraphQLObjectType, GraphQLString } from "graphql";
import { ChallengeInvites } from "../../Entities/ChallengeInvites";
import { CustomDate } from "../CustomTypes/Date";
import { ChallengeType } from "./ChallengeType";
import { UserType } from "./UserType";

export const ChallengeInviteType: GraphQLObjectType<ChallengeInvites> = new GraphQLObjectType({
    name: "ChallengeInvite",
    fields: () => ({
        ChallengeInviteId: { type: GraphQLID },
        State: { type: GraphQLString },
        Date: { type: CustomDate },
        IsRequest: { type: GraphQLBoolean },
        User: { type: UserType },
        Challenge: { type: ChallengeType }
    })
})