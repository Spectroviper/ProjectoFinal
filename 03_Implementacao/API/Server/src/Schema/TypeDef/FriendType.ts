import { GraphQLID, GraphQLObjectType, GraphQLString } from "graphql";
import { CustomDate } from "../CustomTypes/Date";
import { UserType } from "./UserType";
import { Friends } from "../../Entities/Friends";

export const FriendType: GraphQLObjectType<Friends> = new GraphQLObjectType({
    name: "Friend",
    fields: () => ({
        FriendId: { type: GraphQLID },
        State: { type: GraphQLString },
        Date: { type: CustomDate },
        UserSender: { type: UserType },
        UserReceiver: { type: UserType }
    })
})