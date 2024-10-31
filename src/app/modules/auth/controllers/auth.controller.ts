import { Body, Controller, Get, Post, Req, Res, UnauthorizedException, UseGuards } from '@nestjs/common';
import { GoogleAuthGuard } from '../guards/google-auth-guard';
import { AuthService } from '../services/auth.service';
import { User } from 'app/modules/users/entities/user.entity';
import { AuthDto } from '../dtos/auth.ditos';

@Controller('auth')
export class AuthController {
  constructor(private authService: AuthService) { }

  @Get('google')
  @UseGuards(GoogleAuthGuard)
  async googleAuth(@Req() req: any) {
    const user = req.user as User;
    const token = this.authService.login(user);
    return token;
  }

  @Get('google/callback')
  @UseGuards(GoogleAuthGuard)
  async googleAuthCallback(@Req() req: any, @Res() res: any) {
    const token = await this.authService.login(req.user as User);
    return res.redirect(`http://localhost:3000?token=${token.access_token}`);
  }

  @Post('login')
  async login(@Body() authDto: AuthDto) {
    const user = await this.authService.validateUser(authDto.email, authDto.password);
    if (!user) {
      throw new UnauthorizedException('Invalid credentials');
    }
    const token = this.authService.login(user);
    return { access_token: (await token).access_token };
  }

  @Post('logout')
  async logout(@Req() req: Response) {
    const token = req.headers.get('authorization')?.split(' ')[1];
    if (!token) {
      return { statusCode: 400, message: 'Token not provided' };
    }

    const success = await this.authService.logout(token);
    if (success) {
      return { statusCode: 200, message: 'Logout successful' };
    } else {
      return { statusCode: 401, message: 'Invalid token' };
    }
  }
}

