import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {AssessmentTypeComponent} from "./assessment-type.component";
import {AssessmentTypeDetailComponent} from "./assessment-type-detail.component";
import {AssessmentTypePopupComponent} from "./assessment-type-dialog.component";
import {AssessmentTypeDeletePopupComponent} from "./assessment-type-delete-dialog.component";


export const assessmentTypeRoute: Routes = [
	{
		path: 'assessment-type',
		component: AssessmentTypeComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.assessmentType.home.title'
		}
	}, {
		path: 'assessment-type/:id',
		component: AssessmentTypeDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.assessmentType.home.title'
		}
	}
];

export const assessmentTypePopupRoute: Routes = [
	{
		path: 'assessment-type-new',
		component: AssessmentTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.assessmentType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'assessment-type/:id/edit',
		component: AssessmentTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.assessmentType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'assessment-type/:id/delete',
		component: AssessmentTypeDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.assessmentType.home.title'
		},
		outlet: 'popup'
	}
];
