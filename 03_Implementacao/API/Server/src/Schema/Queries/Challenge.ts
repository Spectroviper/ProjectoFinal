import { GraphQLID, GraphQLList, GraphQLString } from 'graphql'
import { ChallengeType } from '../TypeDef/ChallengeType';
import { Challenges } from '../../Entities/Challenges';
import { Users } from '../../Entities/Users';
import { appDataSource } from "../../DataSource";

export const GET_ALL_JOINABLE_CHALLENGES = {
  type: new GraphQLList(ChallengeType),
  description: "Get all Challenges the User can join from the database",
  args: {
    UserId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { UserId } = args;

    const user = await appDataSource
      .getRepository(Users)
      .createQueryBuilder("user")
      .leftJoinAndSelect("user.UserChallenges", "userChallenge")
      .leftJoinAndSelect("userChallenge.Challenge", "challenge")
      .where("user.UserId = :userId", { userId: UserId })
      .getOne();

    if (!user) {
      throw new Error("USER DOES NOT EXIST!");
    }

    const joinedChallengeIds = user.UserChallenges.map(
      (uc) => uc.Challenge.ChallengeId
    );

    const query = appDataSource
      .getRepository(Challenges)
      .createQueryBuilder("challenge");

    if (joinedChallengeIds.length > 0) {
      query.where("challenge.ChallengeId NOT IN (:...ids)", {
        ids: joinedChallengeIds,
      });
    }

    return await query.getMany();
  },
};

export const GET_CHALLENGE_BY_CHALLENGEID = {
  type: ChallengeType,
  description: "Get a certain Challenge from the database with its relations",
  args: {
    ChallengeId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { ChallengeId } = args;

    return await appDataSource.transaction(async (manager) => {
      const challenge = await manager
        .getRepository(Challenges)
        .createQueryBuilder("challenge")
        .leftJoinAndSelect("challenge.ChallengeAchievements", "challengeAchievements")
        .leftJoinAndSelect("challengeAchievements.Achievement", "achievement")
        .leftJoinAndSelect("challenge.UserChallenges", "userChallenges")
        .leftJoinAndSelect("userChallenges.User", "user")
        .where("challenge.ChallengeId = :challengeId", { challengeId: ChallengeId })
        .getOne();

      if (!challenge) {
        throw new Error("CHALLENGE DOES NOT EXIST!");
      }

      return challenge;
    });
  },
};


export const SEARCH_CHALLENGES = {
  type: new GraphQLList(ChallengeType),
  description: "Search Challenges by ChallengeName and Type",
  args: {
    ChallengeName: { type: GraphQLString },
    Type: { type: GraphQLString }
  },
  async resolve(parent: any, args: any) {
    const { ChallengeName, Type } = args;

    try {
      const query = appDataSource
        .getRepository(Challenges)
        .createQueryBuilder('challenge');

      if (ChallengeName) {
        query.andWhere('LOWER(challenge.ChallengeName) LIKE LOWER(:challengeName)', {
          challengeName: `%${ChallengeName}%`
        });
      }

      if (Type) {
        query.andWhere('LOWER(challenge.Type) LIKE LOWER(:type)', {
          type: `%${Type}%`
        });
      }

      return await query.getMany();
    } catch (err) {
      console.error('Search failed:', err);
      throw new Error('Failed to search challenges.');
    }
  }
};