import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {DBCComponent} from "./dbc.component";
import {DBCDetailComponent} from "./dbc-detail.component";
import {DBCPopupComponent} from "./dbc-dialog.component";
import {DBCDeletePopupComponent} from "./dbc-delete-dialog.component";


export const dBCRoute: Routes = [
	{
		path: 'dbc',
		component: DBCComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.dBC.home.title'
		}
	}, {
		path: 'dbc/:id',
		component: DBCDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.dBC.home.title'
		}
	}
];

export const dBCPopupRoute: Routes = [
	{
		path: 'dbc-new',
		component: DBCPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.dBC.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'dbc/:id/edit',
		component: DBCPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.dBC.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'dbc/:id/delete',
		component: DBCDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.dBC.home.title'
		},
		outlet: 'popup'
	}
];
