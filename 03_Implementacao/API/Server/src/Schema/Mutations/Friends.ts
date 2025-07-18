import { GraphQLID } from "graphql";
import { MessageType } from "../TypeDef/Messages";
import { Users } from "../../Entities/Users";
import { Friends } from "../../Entities/Friends";
import { appDataSource } from "../../DataSource";

export const INVITE_FRIEND = {
  type: MessageType,
  description: "Sends a friend request from one user to another",
  args: {
    SenderId:   { type: GraphQLID },
    ReceiverId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { SenderId, ReceiverId } = args;

    if (SenderId === ReceiverId) {
      throw new Error("YOU CANNOT SEND A FRIEND REQUEST TO YOURSELF.");
    }

    return await appDataSource.transaction(async (manager) => {
      const [sender, receiver] = await Promise.all([
        manager.findOne(Users, { where: { UserId: SenderId } }),
        manager.findOne(Users, { where: { UserId: ReceiverId } }),
      ]);

      if (!sender || !receiver) {
        throw new Error("SENDER or RECEIVER DOES NOT EXIST!");
      }

      const existingFriend = await manager
        .createQueryBuilder(Friends, "friends")
        .where(
          `(friends.UserSender = :senderId AND friends.UserReceiver = :receiverId) OR
           (friends.UserSender = :receiverId AND friends.UserReceiver = :senderId)`,
          { senderId: SenderId, receiverId: ReceiverId }
        )
        .getOne();

      if (existingFriend) {
        throw new Error("FRIENDSHIP OR REQUEST ALREADY EXIST.");
      }

      await manager
        .createQueryBuilder()
        .insert()
        .into(Friends)
        .values({
          UserSender: sender,
          UserReceiver: receiver,
          Date: new Date(),
          State: "Pending",
        })
        .execute();

      return { successfull: true, message: "FRIEND INVITE SENT SUCCESSFULLY" };
    });
  },
};


export const ACCEPT_FRIEND_INVITE = {
  type: MessageType,
  description: "Accepts a pending friend request",
  args: {
    FriendId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { FriendId } = args;

    return await appDataSource.transaction(async (manager) => {
      const invite = await manager
        .createQueryBuilder(Friends, "friends")
        .where("friends.FriendId = :FriendId", { FriendId })
        .getOne();

      if (!invite) {
        throw new Error("FRIEND REQUEST NOT FOUND");
      }

      if (invite.State !== "Pending") {
        throw new Error("FRIEND REQUEST IS NOT PENDING");
      }

      await manager
        .createQueryBuilder()
        .update(Friends)
        .set({ State: "Friends" })
        .where("FriendId = :FriendId", { FriendId })
        .execute();

      return {
        successfull: true,
        message: "FRIEND INVITE ACCEPTED",
      };
    });
  },
};


export const REJECT_FRIEND_INVITE = {
  type: MessageType,
  description: "Rejects a pending friend request",
  args: {
    FriendId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { FriendId } = args;

    return await appDataSource.transaction(async (manager) => {
      const invite = await manager
        .createQueryBuilder(Friends, "friends")
        .where("friends.FriendId = :FriendId", { FriendId })
        .getOne();

      if (!invite) {
        throw new Error("FRIEND REQUEST NOT FOUND");
      }

      if (invite.State !== "Pending") {
        throw new Error("FRIEND REQUEST IS NOT PENDING");
      }

      await manager
        .createQueryBuilder()
        .update(Friends)
        .set({ State: "Rejected" })
        .where("FriendId = :FriendId", { FriendId })
        .execute();

      return {
        successfull: true,
        message: "FRIEND INVITE REJECTED",
      };
    });
  },
};


export const DELETE_FRIEND_INVITE = {
  type: MessageType,
  description: "Deletes a friend invite (Accepted, or Rejected)",
  args: {
    FriendId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { FriendId } = args;

    const result = await appDataSource
      .createQueryBuilder()
      .delete()
      .from(Friends)
      .where("FriendId = :FriendId", { FriendId })
      .andWhere("state IN (:...states)", { states: ["Friends", "Rejected"] })
      .execute();

    if (result.affected === 0) {
      throw new Error("FRIEND INVITE NOT FOUND OR NOT IN FRIENDS/REJECTED STATE");
    }

    return { successfull: true, message: "FRIEND INVITE DELETED SUCCESSFULLY" };
  },
};