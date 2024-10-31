import { Injectable } from '@nestjs/common';
import { User } from 'app/modules/users/entities/user.entity';
import { UserService } from 'app/modules/users/services/user.service';
import { JwtService } from '@nestjs/jwt';
import * as bcrypt from 'bcrypt';

@Injectable()
export class AuthService {
  private readonly blackList: string[] = [];
  constructor(
    private readonly userService: UserService,
    private readonly jwtService: JwtService,
  ) { }

  async validateGoogleUser(profile: any): Promise<User> {
    const email = profile.emails[0].value;
    const picture = profile.photos?.[0]?.value;
    let user = await this.userService.findByEmail(email);

    if (!user) {
      user = await this.userService.create({
        name: profile.displayName,
        email,
        image: picture,
      });
    } else if (!user.image && picture) {
      user = await this.userService.update(user.id, { image: picture });
    }

    return user;
  }

  async validateFacebookUser(profile: any): Promise<User> {
    const email = profile.emails[0].value;
    const picture = profile.photos?.[0]?.value;
    let user = await this.userService.findByEmail(email);

    if (!user) {
      user = await this.userService.create({
        name: profile.displayName,
        email,
        image: picture,
      });
    } else if (!user.image && picture) {
      user = await this.userService.update(user.id, { image: picture });
    }

    return user;
  }

  async login(user: User) {
    const payload = {
      username: user.name,
      sub: user.id,
      isAdmin: user.isAdmin,
    };
    return {
      access_token: this.jwtService.sign(payload),
    };
  }

  async validateUser(email: string, password: string): Promise<User | null> {
    const user = await this.userService.findByEmail(email);

    if (user) {
      if (!password) {
        throw new Error('Password is required');
      }

      const isMatch = await bcrypt.compare(password, user.password);
      if (isMatch) {
        return user;
      }
    }

    return null;
  }

  async logout(token: string) {
    this.blackList.push(token);
    return { message: 'Logout successful' };
  }

  isTokenBlacklisted(token: string): boolean {
    return this.blackList.includes(token);
  }


}

