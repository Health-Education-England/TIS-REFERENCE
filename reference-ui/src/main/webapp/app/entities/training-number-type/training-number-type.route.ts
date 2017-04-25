import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {TrainingNumberTypeComponent} from "./training-number-type.component";
import {TrainingNumberTypeDetailComponent} from "./training-number-type-detail.component";
import {TrainingNumberTypePopupComponent} from "./training-number-type-dialog.component";
import {TrainingNumberTypeDeletePopupComponent} from "./training-number-type-delete-dialog.component";


export const trainingNumberTypeRoute: Routes = [
	{
		path: 'training-number-type',
		component: TrainingNumberTypeComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.trainingNumberType.home.title'
		}
	}, {
		path: 'training-number-type/:id',
		component: TrainingNumberTypeDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.trainingNumberType.home.title'
		}
	}
];

export const trainingNumberTypePopupRoute: Routes = [
	{
		path: 'training-number-type-new',
		component: TrainingNumberTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.trainingNumberType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'training-number-type/:id/edit',
		component: TrainingNumberTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.trainingNumberType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'training-number-type/:id/delete',
		component: TrainingNumberTypeDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.trainingNumberType.home.title'
		},
		outlet: 'popup'
	}
];
