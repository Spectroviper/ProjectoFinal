import { GraphQLID } from "graphql";
import { Achievements } from "../../Entities/Achievements";
import { MessageType } from "../TypeDef/Messages";
import { Challenges } from "../../Entities/Challenges";
import { ChallengeAchievements } from "../../Entities/ChallengeAchievements";
import { UserChallengeAchievements } from "../../Entities/UserChallengeAchievements";

export const CREATE_CHALLENGE_ACHIEVEMENT = {
    type: MessageType,
    description: "Creates a Challenge Achievement in the Database",
    args: {
        ChallengeId: { type: GraphQLID },
        AchievementId: { type: GraphQLID }
    },
    async resolve(parent: any, args: any) {
        const { ChallengeId,
            AchievementId} = args;

        const challenge = await Challenges.findOne({where: {ChallengeId: ChallengeId}});
        const achievement = await Achievements.findOne({where: {AchievementId: AchievementId}});

        if(challenge && achievement){
            try{
                const newChallengeAchievement = new ChallengeAchievements();
                newChallengeAchievement.Challenge = challenge;
                newChallengeAchievement.Achievement = achievement;
                await ChallengeAchievements.save(newChallengeAchievement);

                return {successfull: true, message: "CHALLENGE ACHIEVEMENT CREATED SUCCESSFULLY"}
            
            }catch (error: any) {
            if (error.code === 'ER_DUP_ENTRY') {
                throw new Error("CHALLENGE ACHIEVEMENT ALREADY EXISTS!");
            }
            console.error("CREATE_CHALLENGE_ACHIEVEMENT ERROR:", error);
            throw new Error("FAILED TO CREATE CHALLENGE ACHIEVEMENT");
        }
        }
        else{
            throw new Error("CHALLENGE or ACHIEVEMENT DO NOT EXIST");
        }
    }
};


export const DELETE_CHALLENGE_ACHIEVEMENT = {
  type: MessageType,
  description: "Deletes a Challenge Achievement and its related UserChallengeAchievements",
  args: {
    ChallengeAchievementId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const challengeAchievementId = args.ChallengeAchievementId;

    const challengeAchievement = await ChallengeAchievements.findOne({
      where: { ChallengeAchievementId: parseInt(challengeAchievementId) },
      relations: ['Challenge', 'Achievement'],
    });

    if (!challengeAchievement) {
      throw new Error("CHALLENGE ACHIEVEMENT NOT FOUND!");
    }

    await UserChallengeAchievements.createQueryBuilder()
      .delete()
      .where("Challenge = :challengeId AND Achievement = :achievementId", {
        challengeId: challengeAchievement.Challenge.ChallengeId,
        achievementId: challengeAchievement.Achievement.AchievementId,
      })
      .execute();

    await ChallengeAchievements.delete(challengeAchievementId);

    return {
      successfull: true,
      message: "CHALLENGE ACHIEVEMENT AND RELATED USER ENTRIES DELETED SUCCESSFULLY",
    };
  },
};
