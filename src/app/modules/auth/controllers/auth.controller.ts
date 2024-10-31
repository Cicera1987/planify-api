import { Body, Controller, Get, Post, Req, Res, UnauthorizedException, UseGuards } from '@nestjs/common';
import { GoogleAuthGuard } from '../guards/google-auth-guard';
import { AuthService } from '../services/auth.service';
import { User } from 'app/modules/users/entities/user.entity';
import { AuthDto } from '../dtos/auth.ditos';

@Controller('auth')
export class AuthController {
  constructor(private authService: AuthService) {}

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

  @Post('login') // Novo endpoint para login manual
  async login(@Body() authDto: AuthDto) {
    const user = await this.authService.validateUser(authDto.email, authDto.password);
    if (!user) {
      throw new UnauthorizedException('Invalid credentials');
    }
    const token = this.authService.login(user);
    return { access_token: (await token).access_token }; // Retorne o token
}

}
