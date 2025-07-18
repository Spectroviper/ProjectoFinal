import { GraphQLID, GraphQLList, GraphQLString } from "graphql";
import { FriendType } from "../TypeDef/FriendType";
import { Friends } from "../../Entities/Friends";
import { Users } from "../../Entities/Users";
import { appDataSource } from "../../DataSource";
import { Brackets } from "typeorm";

export const GET_ALL_FRIEND_INVITES = {
  type: new GraphQLList(FriendType),
  description: "Get all pending and rejected friend invites for a given User",
  args: {
    UserId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { UserId } = args;

    return await appDataSource.transaction(async (manager) => {
      const user = await manager.findOne(Users, { where: { UserId } });

      if (!user) {
        throw new Error("USER DOES NOT EXIST!");
      }

      const query = manager
        .createQueryBuilder(Friends, "friend")
        .leftJoinAndSelect("friend.UserSender", "sender")
        .leftJoinAndSelect("friend.UserReceiver", "receiver")
        .where("friend.State IN (:...states)", { states: ["Pending", "Rejected"] })
        .andWhere(
          new Brackets(qb =>
            qb.where("sender.UserId = :userId", { userId: +UserId })
              .orWhere("receiver.UserId = :userId", { userId: +UserId })
          )
        );

      const results = await query.orderBy("friend.Date", "DESC").getMany();

      const invites = results.filter(f =>
        f.UserSender.UserId === +UserId ? f.UserReceiver.UserId !== +UserId : f.UserSender.UserId !== +UserId
      );

      if (!invites || invites.length === 0) {
        throw new Error("NO PENDING OR REJECTED INVITES FOUND FOR THIS USER!");
      }

      return invites;
    });
  },
};


export const SEARCH_FRIENDS = {
  type: new GraphQLList(FriendType),
  description: "Search friends by UserName for a given UserId",
  args: {
    UserId: { type: GraphQLID },
    UserName: { type: GraphQLString },
  },
  async resolve(parent: any, args: any) {
    const { UserId, UserName } = args;

    return await appDataSource.transaction(async (manager) => {
      const user = await manager.findOne(Users, { where: { UserId } });

      if (!user) {
        throw new Error("USER DOES NOT EXIST!");
      }

      const query = manager
        .createQueryBuilder(Friends, "friend")
        .leftJoinAndSelect("friend.UserSender", "sender")
        .leftJoinAndSelect("friend.UserReceiver", "receiver")
        .where("friend.State = :state", { state: "Friends" })
        .andWhere(
          new Brackets(qb =>
            qb.where("sender.UserId = :userId", { userId: +UserId })
              .orWhere("receiver.UserId = :userId", { userId: +UserId })
          )
        );

      if (UserName) {
        query.andWhere(
          new Brackets(qb =>
            qb.where("LOWER(sender.UserName) LIKE LOWER(:userName)", { userName: `%${UserName}%` })
              .orWhere("LOWER(receiver.UserName) LIKE LOWER(:userName)", { userName: `%${UserName}%` })
          )
        );
      }

      const results = await query.orderBy("friend.Date", "DESC").getMany();

      const filteredResults = results.filter(f =>
        f.UserSender.UserId === +UserId
          ? f.UserReceiver.UserId !== +UserId
          : f.UserSender.UserId !== +UserId
      );

      return filteredResults;
    });
  },
};