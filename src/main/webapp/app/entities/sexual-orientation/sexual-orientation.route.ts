import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {SexualOrientationComponent} from "./sexual-orientation.component";
import {SexualOrientationDetailComponent} from "./sexual-orientation-detail.component";
import {SexualOrientationPopupComponent} from "./sexual-orientation-dialog.component";
import {SexualOrientationDeletePopupComponent} from "./sexual-orientation-delete-dialog.component";


export const sexualOrientationRoute: Routes = [
	{
		path: 'sexual-orientation',
		component: SexualOrientationComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.sexualOrientation.home.title'
		}
	}, {
		path: 'sexual-orientation/:id',
		component: SexualOrientationDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.sexualOrientation.home.title'
		}
	}
];

export const sexualOrientationPopupRoute: Routes = [
	{
		path: 'sexual-orientation-new',
		component: SexualOrientationPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.sexualOrientation.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'sexual-orientation/:id/edit',
		component: SexualOrientationPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.sexualOrientation.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'sexual-orientation/:id/delete',
		component: SexualOrientationDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.sexualOrientation.home.title'
		},
		outlet: 'popup'
	}
];
