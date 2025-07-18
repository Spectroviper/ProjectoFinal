import { GraphQLID, GraphQLString } from "graphql";
import { MessageType } from "../TypeDef/Messages";
import { Users } from "../../Entities/Users";
import { UserAchievements } from "../../Entities/UserAchievements";
import { UserChallengeAchievements } from "../../Entities/UserChallengeAchievements";
import { UserChallenges } from "../../Entities/UserChallenges";
import { UserGames } from "../../Entities/UserGames";
import { ChallengeInvites } from "../../Entities/ChallengeInvites";
import { UserType } from "../TypeDef/UserType";
import { appDataSource } from "../../DataSource";
import { Friends } from "../../Entities/Friends";


export const CREATE_USER = {
  type: MessageType,
  description: "Create a User in the Database",
  args: {
    UserName: { type: GraphQLString },
    Email: { type: GraphQLString },
    Biography: { type: GraphQLString },
    Image: { type: GraphQLString },
  },
  async resolve(parent: any, args: any) {
    const { UserName, Email, Biography, Image } = args;
    const currentDate = new Date();

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(Email)) {
      throw new Error("INVALID EMAIL FORMAT");
    }

    try {
      return await appDataSource.transaction(async (manager) => {
        await manager
          .createQueryBuilder()
          .insert()
          .into(Users)
          .values({
            UserName,
            Email,
            Biography,
            MemberSince: currentDate,
            LastLogin: currentDate,
            TotalPoints: 0,
            AverageCompletion: 0,
            Image,
          })
          .execute();

        return { successfull: true, message: "USER CREATED SUCCESSFULLY" };
      });
    } catch (error: any) {
      if (error.code === "ER_DUP_ENTRY") {
        throw new Error("USERNAME OR EMAIL ALREADY EXISTS!");
      }
      console.error("CREATE_USER ERROR:", error);
      throw new Error("FAILED TO CREATE USER");
    }
  },
};


export const UPDATE_USER = {
  type: MessageType,
  description: "Update the information of a User in the Database",
  args: {
    UserId: { type: GraphQLID },
    NewUserName: { type: GraphQLString },
    NewBiography: { type: GraphQLString },
    NewImage: { type: GraphQLString },
  },
  async resolve(parent: any, args: any) {
    const { UserId, NewUserName, NewBiography, NewImage } = args;

    const userExists = await appDataSource
      .createQueryBuilder()
      .select("user.UserId")
      .from(Users, "user")
      .where("user.UserId = :userId", { userId: UserId })
      .getOne();

    if (!userExists) {
      throw new Error("USER DOES NOT EXIST!");
    }

    try {
      await appDataSource
        .createQueryBuilder()
        .update(Users)
        .set({
          UserName: NewUserName,
          Biography: NewBiography,
          Image: NewImage,
        })
        .where("UserId = :userId", { userId: UserId })
        .execute();

      return { successfull: true, message: "USER UPDATED SUCCESSFULLY" };
    } catch (error: any) {
      if (error.code === "ER_DUP_ENTRY") {
        throw new Error("USERNAME OR EMAIL ALREADY EXISTS!");
      }
      console.error("UPDATE_USER ERROR:", error);
      throw new Error("FAILED TO UPDATE USER");
    }
  },
};


export const DELETE_USER = {
  type: MessageType,
  description: "Delete a User and its related information from the Database",
  args: {
    UserId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const userIdNum = parseInt(args.UserId);
    if (isNaN(userIdNum)) throw new Error("INVALID USERID");

    const queryRunner = appDataSource.createQueryRunner();
    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      const user = await queryRunner.manager
        .createQueryBuilder(Users, "user")
        .leftJoinAndSelect("user.UserAchievements", "ua")
        .leftJoinAndSelect("user.UserChallengeAchievements", "uca")
        .leftJoinAndSelect("user.UserChallenges", "uc")
        .leftJoinAndSelect("user.UserGames", "ug")
        .leftJoinAndSelect("user.ChallengeInvites", "ci")
        .leftJoinAndSelect("user.FriendSenders", "fs")
        .leftJoinAndSelect("user.FriendReceivers", "fr")
        .where("user.UserId = :userId", { userId: userIdNum })
        .getOne();

      if (!user) {
        throw new Error("USER NOT FOUND");
      }

      await queryRunner.manager
        .createQueryBuilder()
        .delete()
        .from(UserAchievements)
        .where("UserId = :userId", { userId: userIdNum })
        .execute();

      await queryRunner.manager
        .createQueryBuilder()
        .delete()
        .from(UserChallengeAchievements)
        .where("UserId = :userId", { userId: userIdNum })
        .execute();

      await queryRunner.manager
        .createQueryBuilder()
        .delete()
        .from(UserChallenges)
        .where("UserId = :userId", { userId: userIdNum })
        .execute();

      await queryRunner.manager
        .createQueryBuilder()
        .delete()
        .from(UserGames)
        .where("UserId = :userId", { userId: userIdNum })
        .execute();

      await queryRunner.manager
        .createQueryBuilder()
        .delete()
        .from(ChallengeInvites)
        .where("UserId = :userId", { userId: userIdNum })
        .execute();

      await queryRunner.manager
        .createQueryBuilder()
        .delete()
        .from(Friends)
        .where("UserSenderId = :userId OR UserReceiverId = :userId", { userId: userIdNum })
        .execute();

      await queryRunner.manager
        .createQueryBuilder()
        .delete()
        .from(Users)
        .where("UserId = :userId", { userId: userIdNum })
        .execute();

      await queryRunner.commitTransaction();

      return { successfull: true, message: "USER DELETED SUCCESSFULLY" };
    } catch (error) {
      await queryRunner.rollbackTransaction();
      console.error("DELETE USER FAILED:", error);
      return { successfull: false, message: "FAILED TO DELETE USER." };
    } finally {
      await queryRunner.release();
    }
  },
};


export const LOG_IN = {
  type: UserType,
  description: "Logs in a user and returns their basic profile and UserGames",
  args: {
    Email: { type: GraphQLString },
  },
  async resolve(parent: any, args: any) {
    const { Email } = args;

    return await appDataSource.transaction(async (manager) => {
      const user = await manager
        .createQueryBuilder(Users, "user")
        .leftJoinAndSelect("user.UserGames", "ug")
        .leftJoinAndSelect("ug.Game", "game")
        .where("user.Email = :email", { email: Email })
        .getOne();

      if (!user) {
        throw new Error("USER DOES NOT EXIST!");
      }

      user.LastLogin = new Date();
      await manager.save(user);

      return user;
    });
  },
};