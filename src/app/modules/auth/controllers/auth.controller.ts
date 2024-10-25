import { Controller, Get, Req, Res, UseGuards } from '@nestjs/common';
import { GoogleAuthGuard } from '../guards/google-auth-guard';
import { AuthService } from '../services/auth.service';
import { FacebookAuthGuard } from '../guards/facebook-auth-guard';
import { User } from 'app/modules/users/entities/user.entity';

@Controller('auth')
export class AuthController {
    constructor(private authService: AuthService) { }

    @Get('google')
    @UseGuards(GoogleAuthGuard)
    async googleAuth(@Req() req: any) {
        return req.user; // Triggers the redirect to Google login
    }

    @Get('google/callback')
    @UseGuards(GoogleAuthGuard)
    async googleAuthCallback(@Req() req: any, @Res() res: any) {
        const token = await this.authService.login(req.user as User);
        return res.redirect(`http://localhost:3000?token=${token.access_token}`);
    }

    @Get('facebook')
    @UseGuards(FacebookAuthGuard)
    async facebookAuth(@Req() req: any) {
        return req.user; // Triggers the redirect to Facebook login
    }

    @Get('facebook/callback')
    @UseGuards(FacebookAuthGuard)
    async facebookAuthCallback(@Req() req: any, @Res() res: any) {
        const token = await this.authService.login(req.user as User);
        return res.redirect(`http://localhost:3000?token=${token.access_token}`);
    }
}
