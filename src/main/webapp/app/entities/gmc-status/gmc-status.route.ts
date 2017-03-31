import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {GmcStatusComponent} from "./gmc-status.component";
import {GmcStatusDetailComponent} from "./gmc-status-detail.component";
import {GmcStatusPopupComponent} from "./gmc-status-dialog.component";
import {GmcStatusDeletePopupComponent} from "./gmc-status-delete-dialog.component";


export const gmcStatusRoute: Routes = [
	{
		path: 'gmc-status',
		component: GmcStatusComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gmcStatus.home.title'
		}
	}, {
		path: 'gmc-status/:id',
		component: GmcStatusDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gmcStatus.home.title'
		}
	}
];

export const gmcStatusPopupRoute: Routes = [
	{
		path: 'gmc-status-new',
		component: GmcStatusPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gmcStatus.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'gmc-status/:id/edit',
		component: GmcStatusPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gmcStatus.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'gmc-status/:id/delete',
		component: GmcStatusDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gmcStatus.home.title'
		},
		outlet: 'popup'
	}
];
