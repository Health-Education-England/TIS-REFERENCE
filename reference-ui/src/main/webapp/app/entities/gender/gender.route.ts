import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {GenderComponent} from "./gender.component";
import {GenderDetailComponent} from "./gender-detail.component";
import {GenderPopupComponent} from "./gender-dialog.component";
import {GenderDeletePopupComponent} from "./gender-delete-dialog.component";


export const genderRoute: Routes = [
	{
		path: 'gender',
		component: GenderComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gender.home.title'
		}
	}, {
		path: 'gender/:id',
		component: GenderDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gender.home.title'
		}
	}
];

export const genderPopupRoute: Routes = [
	{
		path: 'gender-new',
		component: GenderPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gender.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'gender/:id/edit',
		component: GenderPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gender.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'gender/:id/delete',
		component: GenderDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gender.home.title'
		},
		outlet: 'popup'
	}
];
