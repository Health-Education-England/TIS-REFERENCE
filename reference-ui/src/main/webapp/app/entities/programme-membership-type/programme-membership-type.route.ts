import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {ProgrammeMembershipTypeComponent} from "./programme-membership-type.component";
import {ProgrammeMembershipTypeDetailComponent} from "./programme-membership-type-detail.component";
import {ProgrammeMembershipTypePopupComponent} from "./programme-membership-type-dialog.component";
import {ProgrammeMembershipTypeDeletePopupComponent} from "./programme-membership-type-delete-dialog.component";


export const programmeMembershipTypeRoute: Routes = [
	{
		path: 'programme-membership-type',
		component: ProgrammeMembershipTypeComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.programmeMembershipType.home.title'
		}
	}, {
		path: 'programme-membership-type/:id',
		component: ProgrammeMembershipTypeDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.programmeMembershipType.home.title'
		}
	}
];

export const programmeMembershipTypePopupRoute: Routes = [
	{
		path: 'programme-membership-type-new',
		component: ProgrammeMembershipTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.programmeMembershipType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'programme-membership-type/:id/edit',
		component: ProgrammeMembershipTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.programmeMembershipType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'programme-membership-type/:id/delete',
		component: ProgrammeMembershipTypeDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.programmeMembershipType.home.title'
		},
		outlet: 'popup'
	}
];
