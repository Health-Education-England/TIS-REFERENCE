import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {College} from "./college.model";
import {CollegePopupService} from "./college-popup.service";
import {CollegeService} from "./college.service";
@Component({
	selector: 'jhi-college-dialog',
	templateUrl: './college-dialog.component.html'
})
export class CollegeDialogComponent implements OnInit {

	college: College;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private collegeService: CollegeService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['college']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.college.id !== undefined) {
			this.collegeService.update(this.college)
				.subscribe((res: College) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.collegeService.create(this.college)
				.subscribe((res: College) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: College) {
		this.eventManager.broadcast({name: 'collegeListModification', content: 'OK'});
		this.isSaving = false;
		this.activeModal.dismiss(result);
	}

	private onSaveError(error) {
		this.isSaving = false;
		this.onError(error);
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}

@Component({
	selector: 'jhi-college-popup',
	template: ''
})
export class CollegePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private collegePopupService: CollegePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.collegePopupService
					.open(CollegeDialogComponent, params['id']);
			} else {
				this.modalRef = this.collegePopupService
					.open(CollegeDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
