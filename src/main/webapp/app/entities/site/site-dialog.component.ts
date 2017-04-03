import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Site} from "./site.model";
import {SitePopupService} from "./site-popup.service";
import {SiteService} from "./site.service";
@Component({
	selector: 'jhi-site-dialog',
	templateUrl: './site-dialog.component.html'
})
export class SiteDialogComponent implements OnInit {

	site: Site;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private siteService: SiteService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['site']);
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
		if (this.site.id !== undefined) {
			this.siteService.update(this.site)
				.subscribe((res: Site) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.siteService.create(this.site)
				.subscribe((res: Site) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Site) {
		this.eventManager.broadcast({name: 'siteListModification', content: 'OK'});
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
	selector: 'jhi-site-popup',
	template: ''
})
export class SitePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private sitePopupService: SitePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.sitePopupService
					.open(SiteDialogComponent, params['id']);
			} else {
				this.modalRef = this.sitePopupService
					.open(SiteDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
