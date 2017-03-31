import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Title} from "./title.model";
import {TitlePopupService} from "./title-popup.service";
import {TitleService} from "./title.service";
@Component({
	selector: 'jhi-title-dialog',
	templateUrl: './title-dialog.component.html'
})
export class TitleDialogComponent implements OnInit {

	title: Title;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private titleService: TitleService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['title']);
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
		if (this.title.id !== undefined) {
			this.titleService.update(this.title)
				.subscribe((res: Title) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.titleService.create(this.title)
				.subscribe((res: Title) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Title) {
		this.eventManager.broadcast({name: 'titleListModification', content: 'OK'});
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
	selector: 'jhi-title-popup',
	template: ''
})
export class TitlePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private titlePopupService: TitlePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.titlePopupService
					.open(TitleDialogComponent, params['id']);
			} else {
				this.modalRef = this.titlePopupService
					.open(TitleDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
