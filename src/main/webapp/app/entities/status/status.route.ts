import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {StatusComponent} from "./status.component";
import {StatusDetailComponent} from "./status-detail.component";
import {StatusPopupComponent} from "./status-dialog.component";
import {StatusDeletePopupComponent} from "./status-delete-dialog.component";


export const statusRoute: Routes = [
	{
		path: 'status',
		component: StatusComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.status.home.title'
		}
	}, {
		path: 'status/:id',
		component: StatusDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.status.home.title'
		}
	}
];

export const statusPopupRoute: Routes = [
	{
		path: 'status-new',
		component: StatusPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.status.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'status/:id/edit',
		component: StatusPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.status.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'status/:id/delete',
		component: StatusDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.status.home.title'
		},
		outlet: 'popup'
	}
];
