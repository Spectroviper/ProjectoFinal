import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn, Unique } from "typeorm";
import { Users } from "./Users";

@Entity()
@Unique(['UserSender', 'UserReceiver'])
export class Friends extends BaseEntity {

    @PrimaryGeneratedColumn()
    FriendId!: number;

    @Column({default: "Pending"})
    State!: string;

    @Column()
    Date!: Date;

    @ManyToOne(() => Users, users => users.FriendSenders)
    @JoinColumn({ name: 'UserSender' })
    UserSender!: Users;

    @ManyToOne(() => Users, users => users.FriendReceivers)
    @JoinColumn({ name: 'UserReceiver' })
    UserReceiver!: Users;

}